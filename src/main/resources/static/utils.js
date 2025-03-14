function formatAmount(value) {
  // 숫자 외의 문자는 제거
  let formattedValue = value.replace(/[^\d]/g, '');

  if (formattedValue) {
    return Number(formattedValue).toLocaleString();
  }
  return '';
}

function amountInputListener(amountInput) {
  amountInput.addEventListener("input", function () {
    let value = amountInput.value;
    value = formatAmount(value);

    // 값이 "0"만 입력된 경우 입력을 방지
    if (value === '0') {
      value = '';
    }

    amountInput.value = value;
  });
}

function formatCurrency(value) {
  // 세 자리마다 , 추가
  return value.toLocaleString();
}

function formatDate(dateString) {
  return new Date(new Date(dateString).getTime() + 9 * 60 * 60 * 1000) // 9시간 추가
  .toISOString().replace("T", " ").split(".")[0];
}