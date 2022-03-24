package in.icomputercoding.folkchat.Model;



public class Comment {
    private String profile;
    private String commentId;
    private String comment;

    public Comment() {
    }

    public Comment(String profile, String commentId, String comment) {
        this.profile = profile;
        this.commentId = commentId;
        this.comment = comment;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
