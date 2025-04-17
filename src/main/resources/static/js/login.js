const submit = document.getElementById('submit')

if(submit){
    submit.addEventListener('click',event => {
        fetch(`/auth/login`,{
            method:'POST',
            headers:{
                "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
                "Content-Type":"application/json",
            },
            body:JSON.stringify({
                userId: document.getElementById('username').value,
                password: document.getElementById('password').value
            })
        }).then(res=>{
            if(!res.ok) throw new Error("로그인 실패");
            return res.json();
        }).then(data => {
            localStorage.setItem("accessToken", data.accessToken);
            localStorage.setItem("refreshToken", data.refreshToken);
            setTimeout(() => {
                location.replace("/articles");
            }, 100); //
        })
            .catch(err =>{
                alert("로그인 실패");
            })
    })
}
