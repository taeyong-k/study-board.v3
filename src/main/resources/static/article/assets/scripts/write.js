const $writeForm = document.getElementById('writeForm');
const nicknameRegex = new RegExp('^([\\da-zA-Z가-힣]{2,10})$');
const passwordRegex = new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'",<.>/?]{4,25})$');

$writeForm.onsubmit = (e) => {
    e.preventDefault();

    if ($writeForm['nickname'].value === '') {
        alert('닉네임을 입력해주세요.');
        $writeForm['nickname'].focus();
        return;
    }
    if (!nicknameRegex.test($writeForm['nickname'].value)){
        alert('올바른 닉네임을 입력해주세요.');
        $writeForm['nickname'].focus();
        $writeForm['nickname'].select();
        return;
    }

    if ($writeForm['password'].value === '') {
        alert('비밀번호를 입력해주세요.');
        $writeForm['password'].focus();
        return;
    }
    if (!passwordRegex.test($writeForm['password'].value)){
        alert('올바른 비밀번호를 입력해주세요.');
        $writeForm['password'].focus();
        $writeForm['password'].select();
        return;
    }
    if ($writeForm['passwordCheck'].value === '') {
        alert('비밀번호를 한번 더 입력해주세요.');
        $writeForm['passwordCheck'].focus();
        return;
    }
    if ($writeForm['password'].value !== $writeForm['passwordCheck'].value){
        alert('비밀번호가 일치하지 않습니다.');
        $writeForm['passwordCheck'].focus();
        $writeForm['passwordCheck'].select();
        return;
    }

    if ($writeForm['title'].value === '') {
        alert('제목을 입력해주세요.');
        $writeForm['title'].focus();
        return;
    }
    if ($writeForm['content'].value === '') {
        alert('내용을 입력해주세요.');
        $writeForm['content'].focus();
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append("nickname", $writeForm['nickname'].value);
    formData.append("password", $writeForm['password'].value);
    formData.append("title", $writeForm['title'].value);
    formData.append("content", $writeForm['content'].value);
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
                location.href =`/article/?id=${response.id}`;
                break;
            default:
                alert('알 수 없는 이유로 게시글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('POST', '/article/write');
    xhr.send(formData);
};

