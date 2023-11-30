/**
 * Independent time operation tool to facilitate subsequent switch to dayjs
 */
import dayjs from 'dayjs';

const DATE_TIME_FORMAT = 'YYYY-MM-DD HH:mm:ss';
const DATE_FORMAT = 'YYYY-MM-DD';

export function formatToDateTime(date?: dayjs.ConfigType, format = DATE_TIME_FORMAT): string {
  return dayjs(date).format(format);
}

export function formatToDate(date?: dayjs.ConfigType, format = DATE_FORMAT): string {
  return dayjs(date).format(format);
}

export const getTimestamp = (date) => {
  return (
      date.getFullYear() * 10000000000 +
      (date.getMonth() + 1) * 100000000 +
      date.getDate() * 1000000 +
      date.getHours() * 10000 +
      date.getMinutes() * 100 +
      date.getSeconds()
  ).toString();
};

export const dateUtil = dayjs;
