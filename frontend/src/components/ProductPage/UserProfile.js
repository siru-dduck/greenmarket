import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { FaUserCircle } from "react-icons/fa";
import styled from "styled-components";
import UserContext from "../../util/User.context";
import axios from "axios";

function UserProfile(props){
  const { state: { user } } = useContext(UserContext);

  const onClickChatButton = () => {
    if(!user){
      alert("로그인후 이용해주세요.");
      props.history.push("/login");
    } else{
      if(props.chatRoomId){
        props.history.push(`/chat?room_id=${props.chatRoomId}`);
        return; 
      }
      const { id } = props.match.params;
      axios.post("/api/chat/room",{ article_id: id })
      .then((response)=>{
        console.log(response);
        if(response.data.isSuccess){
          props.history.push(`/chat?room_id=${response.data.chatRoomId}`)
        } else {
          alert("채팅방 생성에 실패했습니다. 다시 시도해주세요.");
        }
      })
      .catch((error)=>{
        console.log(error);
      })
    }
  }
  return (
    <Address>
      <div className="user-profile-address__column">
        <Link to={`/user/${props.user.id}`}>
          <div className="user-profile">
            <div className="user-profile__image">
              {
                props.user.profileImageUrl?
                  <img src={props.user.profileImageUrl} alt="유저의 프로필 사진"/>
                  :
                  <FaUserCircle size="40" color="#bdc3c7"/>
              }
            </div>
            <div className="user-profile__description">
              <div className="user-profile__username">{props.user.nickname}</div>
              <div className="user-profile__region">{props.user.address1} {props.user.address2}</div>
            </div>
          </div>
        </Link>
      </div>
      <div className="user-profile-address__column">
          {
            (user && user.id === props.user.id)?
              null:
              <Button onClick={onClickChatButton}>
                채팅하기
              </Button>
          }
      </div>
    </Address>
  );
}

const Address = styled.address`
  width:659px;
  margin-top:32px;
  padding-bottom:22px;
  border-bottom: 1px solid #dbdbdb;
  display:flex;
  justify-content: space-between;
  .user-profile{
    display:flex;
    .user-profile__image{
      width:40px;
      height:40px;
      margin-right:14px;
      img{
        width:100%;
        height:100%;
      }
    }
    .user-profile__description{
      display:flex;
      flex-direction:column;
      .user-profile__username{
        font-weight:500;
      }
      .user-profile__region{
        font-size:13px;
        color:#757575;
      }
    }
  }
`;

const Button = styled.button`
  padding:12px 31.5px;
  color:#fcfcfc;
  font-size:18px;
  font-weight:500;
  background-color:#1dd1a1;
  border:none;
  border-radius:4px;
  outline:none;
  &:active{
    background-image:linear-gradient(to top, rgba(0, 0, 0, 0.075), rgba(0, 0, 0, 0));
  }
`;

export default UserProfile;