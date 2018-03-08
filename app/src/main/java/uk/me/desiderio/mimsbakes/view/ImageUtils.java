package uk.me.desiderio.mimsbakes.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Utility class to load image using Picasso library.
 * The class sets placeholder and default images when error
 */

public class ImageUtils {
    private static final String TAG = ImageUtils.class.getSimpleName();

    public static void loadImage(Context context,
                                 ImageView view,
                                 String imageUrlString,
                                 int defaultImageRes) {

        boolean isImageUrlStringMissing = imageUrlString == null || imageUrlString.isEmpty();

        if(!isImageUrlStringMissing) {
            Picasso.with(context)
                    .load(imageUrlString)
                    .into(view);
        } else if (view.getDrawable() == null) {
            view.setImageResource(defaultImageRes);
        }
    }

    @IdRes
    public static int getRandomDefaultThumbnailRes(Context context,
                                                   int arrayRes,
                                                   int defaultImageRes) {
        TypedArray placehoders = context.getResources()
                .obtainTypedArray(arrayRes);
        int index =  (int) (Math.random() * placehoders.length());
        int imageResId = placehoders.getResourceId(index, defaultImageRes);
        placehoders.recycle();
        return imageResId;
    }
}
