package in.icomputercoding.folkchat.Model;

public class User {

    private String uid, name, phoneNumber, profileImage, token;
    private int followerCount, followingCount;
    private String profile;

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public User(String uid, String name, String phoneNumber, String imageUrl) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImage = imageUrl;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public User(String uid, String name, String phoneNumber, String profileImage, String token, int followerCount, int followingCount, String profile) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.token = token;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.profile = profile;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public User(String userID, int i, String email, String profileImage) {

    }

    public User() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
