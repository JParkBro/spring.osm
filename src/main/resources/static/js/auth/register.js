class Register {
    constructor() {
        this.isDuplicateChecked = false;
        this.initEventListeners()
    }

    initEventListeners() {
        const $userIdInput = $("#userId");

        $userIdInput.on("input", () => {
            this.isDuplicateChecked = false;
        });

        $(".submit-btn").on("click", (event) => {
            event.preventDefault();

            const form = $("#register-form")[0]
            if (!form.reportValidity()) return;

            if (this.validate()) {
                this.submitForm();
            }
        });
    }

    checkDuplicate() {
        const userId = $("#userId").val();

        if (!userId) {
            alert("아이디를 입력해주세요.");
            return;
        }

        $.ajax({
            url: "/api/v1/auth/checkDuplicate",
            type: "GET",
            data: { userId: userId },
            success: (response) => {
                if (response.isDuplicate) {
                    alert("이미 사용 중인 아이디입니다.")
                } else {
                    alert("사용가능한 아이디 입니다.")
                    this.isDuplicateChecked = true;
                }
            },
            error: (xhr) => {
                console.error("xhr :", xhr.responseText);
                console.error(xhr.status)
                alert("중복 확인에 실패했습니다. 다시 시도해주세요.");
            }
        });
    }

    validate() {
        if (!this.isDuplicateChecked) {
            alert("아이디 중복검사를 해주세요.");
            return false;
        }

        const password = $("#password").val();
        const confirmPassword = $("#confirmPassword").val();
        if (password !== confirmPassword) {
            alert("비밀번호가 일치하지 않습니다.");
            return false;
        }

        return true;
    }

    submitForm() {
        const formData = {
            userId: $("#userId").val(),
            password: $("#password").val(),
            name: $("#name").val(),
            email: $("#email").val(),
            mobilePhone: $("#mobilePhone").val(),
            postalCode: $("#postalCode").val(),
            address: $("#address").val(),
            detailAddress: $("#detailAddress").val(),
            termsAgreed: $("#terms").is(":checked"),
            privacyAgreed: $("#privacy").is(":checked")
        };

        $.ajax({
            url: "/api/v1/auth/register",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(formData),
            success: (response) => {
                alert("회원가입이 완료되었습니다!");
                window.location.href = "/"; // 성공 시 메인 페이지로 리다이렉트
            },
            error: (xhr) => {
                console.error("xhr :", xhr.responseText);
                console.error(xhr.status);
                alert("회원가입에 실패했습니다. 다시 시도해주세요.");
            }
        });
    }
}

const registerJs = new Register();