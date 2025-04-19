const createButton = document.getElementById('create-btn')

if(createButton){
    createButton.addEventListener('click',event => {
        fetch(`/api/products`,{
            method:'POST',
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
                "Content-Type": "application/json"
            },
            body:JSON.stringify({
                name: document.getElementById('name').value,
                price: document.getElementById('price').value,
                stockQuantity: document.getElementById('stockQuantity').value,
                tagNames: document.getElementById('tagNames').value
            })
        }).then(() =>{
            alert("등록이 완료되었습니다.")
            location.replace(`/products`)
        }).catch(err => {
            console.error("상품 등록 실패", err);
        });
    })
}