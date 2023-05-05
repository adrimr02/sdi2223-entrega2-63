module.exports = function (dateString) {
  // Check if the input string matches the DD/MM/YYYY format
  const dateRegex = /^(\d{2})\/(\d{2})\/(\d{4})$/;
  if (!dateRegex.test(dateString)) {
    return false;
  }

  // Split the date string into day, month, and year components
  const [day, month, year] = dateString.split('/');

  // Create a new Date object using the parsed components
  const date = new Date(`${month}/${day}/${year}`);

  // Check if the parsed date is valid and the components match the original input
  return (
    date instanceof Date &&
    !isNaN(date) &&
    date.getDate() === Number(day) &&
    date.getMonth() === Number(month) - 1 &&
    date.getFullYear() === Number(year)
  );
}