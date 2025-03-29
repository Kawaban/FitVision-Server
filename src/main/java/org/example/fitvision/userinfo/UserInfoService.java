package org.example.fitvision.userinfo;

import java.util.List;
import org.example.fitvision.userinfo.domain.UserInfo;
import org.example.fitvision.userinfo.dto.*;

public interface UserInfoService {
    void createUserInfo(UserInfoRequest userInfoRequest);

    UserInfoResponse getUserInfoWithOutTasks();

    UserInfoUpdateDataResponse updateUserInfoData(UserInfoUpdateDataRequest userInfoUpdateDataRequest);

    void addFriend(UserInfo sender, UserInfo recipient);

    UserInfo getUserInfoByEmail(String email);

    List<FriendResponse> getFriends();

    void deleteFriend(DeleteFriendRequest deleteFriendRequest);

    void wonDay(UserInfo userInfo);

    void lostDay(UserInfo userInfo);

    List<UserInfo> getAllUsers();
}
