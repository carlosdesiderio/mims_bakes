package uk.me.desiderio.mimsbakes.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Utility class to load image using Picasso library. The class sets placeholder and default
 * images when error
 */

public class ImageUtils {
    private static final String TAG = ImageUtils.class.getSimpleName();

    public static void loadImage(Context context,
                                 ImageView view,
                                 String imageUrl,
                                 int placeholderRes,
                                 int defaultImageRes) {
        Log.d(TAG, " patito :: defaultImageRes " + defaultImageRes +
                "for  image  " + imageUrl);

        if(imageUrl == null || imageUrl.isEmpty()) {
            view.setImageResource(defaultImageRes);
        } else {

            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(placeholderRes)
                    .error(defaultImageRes)
                    .into(view);

        }
    }

    public static int getRandomDefaultThumbnailRes(Context context, int arrayRes, int
            defaultImageRes) {
        TypedArray placehoders = context.getResources().obtainTypedArray(arrayRes);
        int index =  (int) (Math.random() * placehoders.length());
        return placehoders.getResourceId(index, defaultImageRes);
    }
}
