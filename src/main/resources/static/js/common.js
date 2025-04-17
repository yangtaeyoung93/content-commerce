const isLogout = document.getElementById('logout-btn')
if(isLogout){
    isLogout.addEventListener('click',event => {
        fetch("/auth/logout", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                refreshToken: localStorage.getItem("refreshToken")
            })
        }).then(() => {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            location.replace("/login");
        });
    })
}