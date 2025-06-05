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
