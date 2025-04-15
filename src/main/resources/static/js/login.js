const submit = document.getElementById('submit')

if(submit){
    submit.addEventListener('click',event => {
        fetch(`/auth/login`,{
            method:'POST',
            headers:{
                "Content-Type":"application/json",
            },
            body:JSON.stringify({
                username: document.getElementById('username').value,
                password: document.getElementById('password').value
            })
        }).then(() =>{
            alert("환영 합니다.")
            location.replace(`/articles`)
        })
    })
}