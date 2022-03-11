package in.icomputercoding.folkchat.Model;


public class Post {
    private String postId;
    private String postImage;
    private String postProfile;
    private String postDescription;

    public Post() {
    }

    public Post(String postId, String postImage, String postProfile, String postDescription) {
        this.postId = postId;
        this.postImage = postImage;
        this.postProfile = postProfile;
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

    public String getPostProfile() {
        return postProfile;
    }

    public void setPostProfile(String postProfile) {
        this.postProfile = postProfile;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }
}

