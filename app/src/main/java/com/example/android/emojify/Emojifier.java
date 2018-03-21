package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

/**
 * Created by pavin on 21.03.2018.
 */

public class Emojifier {

    static Paint mPaint;
    static Canvas mCanvas;

    private static final String LOG_TAG = Emojifier.class.getSimpleName();

    public static void detectFaces(Context context, Bitmap bitmap){
        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
       // if(!faceDetector.isOperational()) {
            SparseArray<Face> faces = faceDetector.detect(frame);
           /* for (int i = 0; i < faces.size(); i++) {
                Face face = faces.valueAt(i);
                for (Landmark landmark :
                        face.getLandmarks()) {
                    int x = (int) (landmark.getPosition().x * 3);
                    int y = (int) (landmark.getPosition().y * 3);
                 //   getCanvas(bitmap).drawCircle(x, y, 10, getPaint());
                }
            }*/
        //}
        Toast.makeText(context, "faces found: " + faces.size(), Toast.LENGTH_SHORT).show();
        faceDetector.release();
    }

    private static Canvas getCanvas(Bitmap bitmap){
        if(mCanvas != null){
            mCanvas.setBitmap(bitmap);
        }
        else {
            mCanvas = new Canvas(bitmap);
        }
        return mCanvas;
    }

    private static Paint getPaint(){
        if(mPaint == null){
            mPaint = new Paint();
            mPaint.setColor(Color.CYAN);
            mPaint.setStyle(Paint.Style.STROKE);
        }
        return mPaint;
    }

}
