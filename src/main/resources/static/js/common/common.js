function updateCartItemCount() {
    $.ajax({
        url: '/api/v1/cart/count',
        type: 'GET',
        success: function(count) {
            // 배지 요소 찾기
            const $cartBadge = $('.cart-badge');

            if (count > 0) {
                // 카운트가 있으면 배지 표시 또는 업데이트
                if ($cartBadge.length) {
                    // 배지가 이미 있으면 내용만 업데이트
                    $cartBadge.text(count);
                } else {
                    // 배지가 없으면 새로 추가
                    $('.cart-icon-container').append('<span class="cart-badge">' + count + '</span>');
                }
            } else {
                // 카운트가 0이면 배지 제거
                $cartBadge.remove();
            }
        },
        error: function(xhr, status, error) {
            console.error('장바구니 개수 조회 실패:', error);
        }
    });
}