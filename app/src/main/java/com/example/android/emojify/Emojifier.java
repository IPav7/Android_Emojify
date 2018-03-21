package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
    private static final double EYE_THRESHOLD = 0.5d;
    private static final double SMILE_THRESHOLD = 0.15d;

    private static final String LOG_TAG = Emojifier.class.getSimpleName();

    public static void detectFacesAndOverlayEmoji(Context context, Bitmap bitmap){
        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);
       // Drawable emojiBitmap;
            for (int i = 0; i < faces.size(); i++) {
                //      switch (
                whichEmoji(context, faces.valueAt(i), i);
            }
               /*     case SMILE:
                        break;
                    case FROWN:
                        break;
                    case LEFT_WINK:
                        break;
                    case RIGHT_WINK:
                        break;
                    case LEFT_WINK_FROWN:
                        break;
                    case CLOSED_EYE_FROWN:
                        break;
                    case CLOSED_EYE_SMILE:
                        break;
                    case RIGHT_WINK_FROWN:
                        break;
                }
                }*/
        faceDetector.release();
    }

    private static void whichEmoji(Context context, Face face, int i){
        Emoji emoji;

      /*  Toast.makeText(context,
                "face number " + i + ":\n"
                + " Left Eye: " + face.getIsLeftEyeOpenProbability() + "\n"
                + " Right Eye: " + face.getIsRightEyeOpenProbability() + "\n"
                + " Smiling: " + face.getIsSmilingProbability(),
                     Toast.LENGTH_SHORT).show();*/

        boolean leftEyeOpen = face.getIsLeftEyeOpenProbability() > EYE_THRESHOLD;
        boolean rightEyeOpen = face.getIsRightEyeOpenProbability() > EYE_THRESHOLD;
        boolean isSmiling = face.getIsSmilingProbability() > SMILE_THRESHOLD;

        if(isSmiling){
            if(leftEyeOpen && rightEyeOpen)
                emoji = Emoji.SMILE;
            else if(leftEyeOpen)
                emoji = Emoji.RIGHT_WINK;
            else if(rightEyeOpen)
                emoji = Emoji.LEFT_WINK;
            else emoji = Emoji.CLOSED_EYE_SMILE;
          }
        else {
            if(leftEyeOpen && rightEyeOpen)
                emoji = Emoji.FROWN;
            else if(leftEyeOpen)
                emoji = Emoji.RIGHT_WINK_FROWN;
            else if(rightEyeOpen)
                emoji = Emoji.LEFT_WINK_FROWN;
            else emoji = Emoji.CLOSED_EYE_FROWN;
        }
        Toast.makeText(context, "Face num" + i + " Emoji is " + emoji.name(), Toast.LENGTH_SHORT).show();
       // return emoji;
    }



    private enum Emoji {
        SMILE,
        FROWN,
        LEFT_WINK,
        RIGHT_WINK,
        LEFT_WINK_FROWN,
        RIGHT_WINK_FROWN,
        CLOSED_EYE_SMILE,
        CLOSED_EYE_FROWN
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
