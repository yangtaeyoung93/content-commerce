const deleteButton = document.getElementById('delete-btn')

if(deleteButton){
    deleteButton.addEventListener('click',event => {
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`,{
            method:'DELETE',
            headers:{
                "Authorization": `Bearer ${localStorage.getItem("accessToken")}`
            }
        }).then(()=>{
            alert("삭제가 완료되었습니다.");
            location.replace('/articles')
        })
    })
}

const modifyButton = document.getElementById('modify-btn')

if(modifyButton){
    modifyButton.addEventListener('click', e=>{
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`/api/articles/${id}`,{
            method:'PUT',
            headers:{
                "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
                "Content-Type":"application/json",
            },
            body:JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value,
                tagNames: document.getElementById('tagNames').value
            })
        }).then(()=>{
            alert("수정이 완료되었습니다.")
            location.replace(`/articles/${id}`)
        })
    })
}

const createButton = document.getElementById('create-btn')

if(createButton){
    createButton.addEventListener('click',event => {
        fetch(`/api/articles`,{
            method:'POST',
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
                "Content-Type": "application/json"
            },
            body:JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value,
                tagNames: document.getElementById('tagNames').value
            })
        }).then(res =>{
            if (!res.ok) {
                return res.json().then( err => {
                    if(`${err.error}` == 403 ){
                        alert(`등록 실패: 권한이 없습니다.`);
                        throw new Error('등록 실패');
                    }
                })
            }
            alert("등록이 완료되었습니다.");
            location.replace(`/articles`)
        }).catch(err => {
            console.error("네트워크 오류", err);
        });
    })
}



document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("accessToken");
    const articlesContainer = document.querySelector('.container .row-6'); // 게시글이 들어갈 div 선택

    // fetch API로 게시글 목록 가져오기
    fetch('/api/articles', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`,  // JWT 토큰을 Authorization 헤더에 담아 전송
        }
    })
        .then(res=>{
            if (!res.ok) {
                throw new Error();
            }

            return res.json();
        })  // 서버 응답을 JSON 형식으로 파싱
        .then(data => {
            data.articles.forEach(article => {
                const articleCard = document.createElement('div');
                articleCard.classList.add('card');

                // 카드 헤더: 게시글 ID
                const cardHeader = document.createElement('div');
                cardHeader.classList.add('card-header');
                cardHeader.innerText = article.id;

                // 카드 본문
                const cardBody = document.createElement('div');
                cardBody.classList.add('card-body');

                // 게시글 제목
                const cardTitle = document.createElement('h5');
                cardTitle.classList.add('card-title');
                cardTitle.innerText = article.title;

                // 게시글 내용
                const cardContent = document.createElement('p');
                cardContent.classList.add('card-content');
                cardContent.innerText = article.content;

                // 태그들
                const tagContainer = document.createElement('div');
                tagContainer.classList.add('row-6');
                article.tags.forEach(tag => {
                    const tagElement = document.createElement('span');
                    tagElement.classList.add('card-content');
                    tagElement.innerHTML = tag.name + '&nbsp;';
                    tagContainer.appendChild(tagElement);
                });

                // '보러 가기' 버튼
                const viewButton = document.createElement('a');
                viewButton.classList.add('btn', 'btn-primary');
                viewButton.href = `/articles/${article.id}`;  // 게시글 상세보기 링크
                viewButton.innerText = '보러 가기';

                // 카드 본문에 제목, 내용, 태그, 버튼 추가
                cardBody.appendChild(cardTitle);
                cardBody.appendChild(cardContent);
                cardBody.appendChild(tagContainer);
                cardBody.appendChild(viewButton);

                // 카드 헤더와 본문을 카드에 추가
                articleCard.appendChild(cardHeader);
                articleCard.appendChild(cardBody);

                // 게시글 목록에 카드 추가
                articlesContainer.appendChild(articleCard);
            });
        })
        .catch(err => {
            console.error("게시글 로딩 실패", err);
        });
});