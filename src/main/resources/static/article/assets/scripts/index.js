const $container = document.getElementById('container');
const $deleteButton = $container.querySelector('button[name="delete"]');
const $password = $container.querySelector('input[name="password"]');
const passwordRegex = new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'",<.>/?]{4,25})$');

$deleteButton.addEventListener('click', () => {
    if ($password.value === '') {
        alert('비밀번호를 입력해주세요.');
        $password.focus();
        return;
    }
    if (!passwordRegex.test($password.value)) {
        alert('올바른 비밀번호를 입력해주세요.');
        $password.focus();
        $password.select();
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('id', new URL(location.href).searchParams.get('id'));
    formData.append("password", $password.value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert('요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure':
                alert('알 수 없는 이유로 작성에 실패하였습니다.');
                break;
            case 'success':
                alert('게시글을 삭제 완료하였습니다. 게시글 작성페이지로 이동합니다.');
                location.href = '/article/write'
                break;
            default:
                alert('알 수 없는 이유로 게시글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('DELETE', '/article/');
    xhr.send(formData);
});

// add comment
const $commentForm = document.getElementById('commentForm');
const $commentContainer = document.getElementById('commentContainer');

const loadComments = () => {
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert('요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const comments = JSON.parse(xhr.responseText);
        if (comments.length === 0) {
            $commentContainer.innerHTML = '등록된 댓글이 없습니다.';
            return;
        }
        $commentContainer.innerHTML = '';
        for (const comment of comments) {
            $commentContainer.insertAdjacentHTML('beforeend', `
                <div class="comment ${comment['deleted'] === true ? 'deleted' : ''}">
                    <div class="head">
                        <span class="writer">${comment['nickname']}</span>
                        <span class="timestamp">${comment['createdAt'].split('T').join(' ')}</span>
                        <span class="-flex-stretch"></span>
                        <a class="action delete" data-id="${comment['id']}">삭제</a>
                    </div>
                    <div class="body">
                        <span class="content">${comment['deleted'] === true ? '(삭제된 댓글입니다.)' : comment['content']}</span>
                    </div>
                </div>`)
        }
    };
    xhr.open('GET', `${origin}/article/comment?id=` + new URL(location.href).searchParams.get('id'));
    xhr.send();
}

const deleteComment = (commentId) => {
    if (!confirm('정말로 댓글을 삭제할까요? 삭제된 댓글은 복구가 어렵습니다.')) {
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('id', commentId);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert('요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure':
                alert('알 수 없는 이유로 댓글 삭제에 실패하였습니다.');
                break;
            case 'success':
                alert('댓글을 성공적으로 삭제하였습니다.');
                loadComments();
                break;
            default:
                alert('알 수 없는 이유로 댓글을 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('DELETE', '/article/comment');
    xhr.send(formData);
}

$commentContainer.addEventListener('click', (e) => {
    if (e.target.classList.contains('delete')) {
        e.preventDefault();
        const commentId = e.target.dataset.id;
        if (commentId) {
            deleteComment(commentId);
        }
    }
});

const writeComment = (args) => {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('articleId', args['articleId']);
    formData.append('nickname', args['nickname']);
    formData.append('content', args['content']);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert('요청을 처리하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        switch (response.result) {
            case 'failure':
                alert('알 수 없는 이유로 댓글 작성에 실패하였습니다.');
                break;
            case 'success':
                loadComments();
                $commentForm['nickname'].value = '';
                $commentForm['content'].value = '';
                break;
            default:
                alert('알 수 없는 이유로 댓글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('POST', '/article/comment');
    xhr.send(formData);
}

$commentForm.addEventListener('submit', (e) => {
    e.preventDefault();
    if ($commentForm['content'].value === '' ) {
        alert('댓글을 입력해 주세요.');
        return;
    }
    writeComment({
        articleId: new URL(location.href).searchParams.get('id'),
        nickname: $commentForm['nickname'].value,
        content: $commentForm['content'].value
    });
});

loadComments();












