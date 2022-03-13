package in.icomputercoding.folkchat.Model;


public class Post {
    private String postId;
    private String postImage;
    private String profile;
    private String postDescription;

    public Post() {
    }

    public Post(String postId, String postImage, String profile, String postDescription) {
        this.postId = postId;
        this.postImage = postImage;
        this.profile = profile;
        this.postDescription = postDescription;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }
}

