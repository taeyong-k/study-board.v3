const $modifyForm = document.getElementById('modifyForm');
const passwordRegex = new RegExp('^([\\da-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:\'",<.>/?]{4,25})$');
const titleRegex = new RegExp('^(.{1,100})$');
const contentRegex = new RegExp('^(.{1,100000})$');

$modifyForm.onsubmit = (e) => {
    e.preventDefault();

    if ($modifyForm['password'].value === '') {
        alert('비밀번호를 입력해 주세요.');
        $modifyForm['password'].focus();
        return;
    }
    if (!passwordRegex.test($modifyForm['password'].value)) {
        alert('올바른 비밀번호를 입력해주세요.');
        $modifyForm['password'].focus();
        $modifyForm['password'].select();
        return;
    }
    if ($modifyForm['title'].value === '') {
        alert('제목을 입력해 주세요.');
        $modifyForm['title'].focus();
        return;
    }
    if (!titleRegex.test($modifyForm['title'].value)) {
        alert('올바른 제목을 입력해주세요.');
        $modifyForm['title'].focus();
        $modifyForm['title'].select();
        return;
    }
    if ($modifyForm['content'].value === '') {
        alert('내용을 입력해 주세요.');
        $modifyForm['content'].focus();
        return;
    }
    if (!contentRegex.test($modifyForm['content'].value)) {
        alert('올바른 내용을 입력해주세요.');
        $modifyForm['content'].focus();
        $modifyForm['content'].select();
        return;
    }
    
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('id', new URL(location.href).searchParams.get('id'));
    formData.append('password', $modifyForm['password'].value);
    formData.append('title', $modifyForm['title'].value);
    formData.append('content', $modifyForm['content'].value);
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
            case 'failure_password':
                alert('비밀번호가 올바르지 않습니다.');
                break;
            case 'success':
                location.href =`/article/?id=${response.id}`;
                break;
            default:
                alert('알 수 없는 이유로 게시글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('PATCH', '/article/');
    xhr.send(formData);
};