package uk.me.desiderio.mimsbakes.data.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * holds data to every step of a recipe
 */

public class Step implements Parcelable {

    public static final String NODE_NAME_STEP_ID = "stepId";
    public static final String NODE_NAME_STEP_SHORT_DESC = "shortDescription";
    public static final String NODE_NAME_STEP_DESC = "description";
    public static final String NODE_NAME_STEP_VIDEO_URL = "videoURL";
    public static final String NODE_NAME_STEP_THUMBNAIL_URL = "thumbnailURL";


    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step() {}

    public Step(int stepId, String shortDescription, String description, String videoURLString, String thumbnailURLString) {
        this.id = stepId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURLString;
        this.thumbnailURL = thumbnailURLString;
    }

    public Step(Parcel in) {
        this.id = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }

    /** returns step id as described in the JSON response
     * This value also reflects the ORDER in which the step have to be carried out */
    public int getStepId() {
        return id;
    }

    public void setStepId(int stepId) {
        this.id = stepId;
    }

    /** returns step short description as a {@link String} */
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    /** returns step long description as a {@link String} */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** returns step video url as a {@link String} */
    public String getVideoURLString() {
        return videoURL;
    }

    public void setVideoURLString(String videoURLString) {
        this.videoURL = videoURLString;
    }

    /** returns step thumbnail url as a {@link String} */
    public String getThumbnailURLString() {
        return thumbnailURL;
    }

    public void setThumbnailURLString(String thumbnailURLString) {
        this.thumbnailURL = thumbnailURLString;
    }

    /** returns a {@link android.content.ContentValues} object with the step's property value
     * to be stored in the local database.
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(NODE_NAME_STEP_ID, getStepId());
        values.put(NODE_NAME_STEP_SHORT_DESC, getShortDescription());
        values.put(NODE_NAME_STEP_DESC, getDescription());
        values.put(NODE_NAME_STEP_VIDEO_URL, getVideoURLString());
        values.put(NODE_NAME_STEP_THUMBNAIL_URL, getThumbnailURLString());
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }


    @SuppressWarnings("unused")
        public static final Creator<Step> CREATOR
                = new Creator<Step>() {
            public Step createFromParcel(Parcel in) {
                return new Step(in);
            }

            public Step[] newArray(int size) {
                return new Step[size];
            }
        };
}
