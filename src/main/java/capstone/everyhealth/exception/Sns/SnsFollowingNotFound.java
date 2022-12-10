package capstone.everyhealth.exception.Sns;

public class SnsFollowingNotFound extends Exception{
    public SnsFollowingNotFound(Long followedId, Long followingId){
        super("followed 멤버 id: "+followedId+", followingId 멤버 : id"+followingId+"인 SnsFollowing이 존재하지 않음");
    }
}
