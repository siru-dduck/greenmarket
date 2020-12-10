import React, { useState } from "react";

const UserContext = React.createContext({
    state: {user: JSON.parse(localStorage.getItem("user"))},
    actions:{
        setUser: ()=>{}
    }
});

const UserProvider = ({children}) =>{
    const [user, setUser] = useState(JSON.parse(localStorage.getItem("user"))); 

    const value = {
        state: {user},
        actions: {setUser}
    };

    return (
        <UserContext.Provider value={value}>{children}</UserContext.Provider>
    )
};

const {Consumer: userConsumer} = UserContext;
export {UserProvider, userConsumer};
export default UserContext;