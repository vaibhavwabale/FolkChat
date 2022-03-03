package in.icomputercoding.folkchat.Model;


public class Posts {
    int profile, postImg;
    String user_name, bio, like, comment, share;

    public Posts(int profile, int postImg, String user_name, String bio, String like, String comment, String share) {
        this.profile = profile;
        this.postImg = postImg;
        this.user_name = user_name;
        this.bio = bio;
        this.like = like;
        this.comment = comment;
        this.share = share;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public int getPostImg() {
        return postImg;
    }

    public void setPostImg(int postImg) {
        this.postImg = postImg;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }
}

