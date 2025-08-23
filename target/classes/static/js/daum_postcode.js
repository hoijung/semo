function openDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
    	location.href = "about:blank?selectedAddress=" + (data.roadAddress) + "/" + (data.zonecode);
        }
    }).open();
}

// 페이지 로드 시 자동 실행
window.onload = function() {
    openDaumPostcode();
};
