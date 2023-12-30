package com.example.testproject.classifier;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import org.tensorflow.lite.Delegate;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.nnapi.NnApiDelegate;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Classifier {
    private Delegate delegate;
    private Interpreter interpreter;
    private Tensor inputTensor;
    private Tensor outputTensor;
    private Size inputShape;
    private int[] imagePixels;
    private ByteBuffer imageBuffer;
    private TensorBuffer outputBuffer;

    public Classifier(Context context, Device device, int numThreads) {
        switch (device) {
            case CPU:
                delegate = null;
                break;
            case NNAPI:
                delegate = new NnApiDelegate();
                break;
            case GPU:
                delegate = new GpuDelegate();
                break;
        }

        try {
            if(delegate == null){
                interpreter = new Interpreter(
                        FileUtil.loadMappedFile(context, MODEL_FILE_NAME),
                        new Interpreter.Options().setNumThreads(numThreads)
                );
            }else{
                interpreter = new Interpreter(
                        FileUtil.loadMappedFile(context, MODEL_FILE_NAME),
                        new Interpreter.Options().setNumThreads(numThreads).addDelegate(delegate)
                );
            }

        } catch (IOException e) {
            // IOException 처리
            e.printStackTrace();
            // 필요한 추가적인 예외 처리 코드
        }
        inputTensor = interpreter.getInputTensor(0);
        outputTensor = interpreter.getOutputTensor(0);
        inputShape = new Size(inputTensor.shape()[2], inputTensor.shape()[1]);
        imagePixels = new int[inputShape.getHeight() * inputShape.getWidth()];
        imageBuffer = ByteBuffer.allocateDirect(4 * inputShape.getHeight() * inputShape.getWidth());
        imageBuffer.order(ByteOrder.nativeOrder());
        outputBuffer = TensorBuffer.createFixedSize(outputTensor.shape(), outputTensor.dataType());

//        Log.v(LOG_TAG, "[Input] shape = " + inputTensor.shape().contentToString() +
//                ", dataType = " + inputTensor.dataType());
//        Log.v(LOG_TAG, "[Output] shape = " + outputTensor.shape().contentToString() +
//                ", dataType = " + outputTensor.dataType());
    }
    public Classifier(Context context) {
        this(context, Device.CPU, 4);
    }

    public Recognition classify(Bitmap image) {
        convertBitmapToByteBuffer(image);
        long start = SystemClock.uptimeMillis();
        interpreter.run(imageBuffer, outputBuffer.getBuffer().rewind());
        long end = SystemClock.uptimeMillis();
        long timeCost = end - start;
        float[] probs = outputBuffer.getFloatArray();
        int top = argMax(probs);
        Log.v(LOG_TAG, "classify(): timeCost = " + timeCost + ", top = " + top +
                ", probs = " + java.util.Arrays.toString(probs));
        return new Recognition(top, probs[top], timeCost);
    }

    public void close() {
        interpreter.close();
        if (delegate instanceof Closeable) {
            try {
                ((Closeable) delegate).close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        imageBuffer.rewind();
        bitmap.getPixels(imagePixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < inputShape.getWidth() * inputShape.getHeight(); i++) {
            int pixel = imagePixels[i];
            imageBuffer.putFloat(convertPixel(pixel));
        }
    }

    private float convertPixel(int color) {
        return (255 - (((color >> 16) & 0xFF) * 0.299f
                + ((color >> 8) & 0xFF) * 0.587f
                + (color & 0xFF) * 0.114f)) / 255.0f;
    }

    private static final String LOG_TAG = Classifier.class.getSimpleName();
    private static final String MODEL_FILE_NAME = "mnist.tflite";

    public static int argMax(float[] array) {
        int maxIndex = -1;
        float maxValue = Float.MIN_VALUE;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
                maxIndex = i;
            }
        }
        if (maxIndex == -1) {
            throw new IllegalArgumentException("Cannot find arg max in empty list");
        }
        return maxIndex;
    }

    public Size getInputShape(){
        return inputShape;
    }
}


