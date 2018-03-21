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

    private static final float EMOJI_SCALE_FACTOR = .9f;
    private static final double EYE_THRESHOLD = 0.5d;
    private static final double SMILE_THRESHOLD = 0.15d;

    private static final String LOG_TAG = Emojifier.class.getSimpleName();

    public static Bitmap detectFacesAndOverlayEmoji(Context context, Bitmap bitmap){
        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);
        Bitmap emojiBitmap = null;
            for (int i = 0; i < faces.size(); i++) {
                      switch (whichEmoji(faces.valueAt(i))){
                    case SMILE:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.smile);
                        break;
                    case FROWN:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.frown);
                        break;
                    case LEFT_WINK:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwink);
                        break;
                    case RIGHT_WINK:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwink);
                        break;
                    case LEFT_WINK_FROWN:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.leftwinkfrown);
                        break;
                    case CLOSED_EYE_FROWN:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.closed_frown);
                        break;
                    case CLOSED_EYE_SMILE:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.closed_smile);
                        break;
                    case RIGHT_WINK_FROWN:
                        emojiBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.rightwinkfrown);
                        break;
                }
                bitmap = addBitmapToFace(bitmap, emojiBitmap, faces.valueAt(i));
                }
        faceDetector.release();
            return bitmap;
    }

    /**
     * Combines the original picture with the emoji bitmaps
     *
     * @param backgroundBitmap The original picture
     * @param emojiBitmap      The chosen emoji
     * @param face             The detected face
     * @return The final bitmap, including the emojis over the faces
     */
    private static Bitmap addBitmapToFace(Bitmap backgroundBitmap, Bitmap emojiBitmap, Face face) {

        // Initialize the results bitmap to be a mutable copy of the original image
        Bitmap resultBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),
                backgroundBitmap.getHeight(), backgroundBitmap.getConfig());

        // Scale the emoji so it looks better on the face
        float scaleFactor = EMOJI_SCALE_FACTOR;

        // Determine the size of the emoji to match the width of the face and preserve aspect ratio
        int newEmojiWidth = (int) (face.getWidth() * scaleFactor);
        int newEmojiHeight = (int) (emojiBitmap.getHeight() *
                newEmojiWidth / emojiBitmap.getWidth() * scaleFactor);


        // Scale the emoji
        emojiBitmap = Bitmap.createScaledBitmap(emojiBitmap, newEmojiWidth, newEmojiHeight, false);

        // Determine the emoji position so it best lines up with the face
        float emojiPositionX =
                (face.getPosition().x + face.getWidth() / 2) - emojiBitmap.getWidth() / 2;
        float emojiPositionY =
                (face.getPosition().y + face.getHeight() / 2) - emojiBitmap.getHeight() / 3;

        // Create the canvas and draw the bitmaps to it
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        canvas.drawBitmap(emojiBitmap, emojiPositionX, emojiPositionY, null);

        return resultBitmap;
    }

    private static Emoji whichEmoji(Face face){
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
        return emoji;
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

}
