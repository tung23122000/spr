package dts.com.vn.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {

  public static final String DD_MM_YYYY_HH_mm_ss = "dd/MM/yyyy HH:mm:ss";

  public static String formatInstant(Instant input, String format) {
    if (input == null) {
      return null;
    } else {
      DateTimeFormatter dateTimeFormatter =
          DateTimeFormatter.ofPattern(format).withZone(ZoneId.systemDefault());
      return dateTimeFormatter.format(input);
    }
  }

  public static Date convertStringToDate(String input, String format) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    sdf.setLenient(false);
    return sdf.parse(input);
  }

  public static String convertDateToString(Date input, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    sdf.setLenient(false);
    return sdf.format(input);
  }

  public static Instant convertStringToInstantUtc(String dateStr, String format) {
    return convertStringToInstant(dateStr, format, null);
  }

  public static Instant convertStringToInstant(String dateStr, String format, ZoneId zoneId) {
    if (dateStr == null || format == null)
      return null;
    if (zoneId == null) {
      zoneId = ZoneId.of("UTC");
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    try {
      LocalDate localDate = LocalDate.parse(dateStr, formatter);
      return localDate.atStartOfDay(zoneId).toInstant();
    } catch (Exception e) {
      return null;
    }
  }

  public static Instant convertStringToInstant(String dateStr, String format) {
    if (dateStr == null || format == null)
      return null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);

    TemporalAccessor temporalAccessor = formatter.parse(dateStr);
    LocalDateTime localDateTime = LocalDateTime.from(temporalAccessor);
    ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
    Instant result = Instant.from(zonedDateTime);
    return result;
  }

  public static String formatInstantWithTimezone(Instant input, String format, String timezone) {
    if (input == null) {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    ZoneId zone = ZoneId.systemDefault();
    try {
      if (timezone != null) {
        zone = ZoneId.of(timezone);
      }
      ZonedDateTime zoneDateTime = input.atZone(zone);
      return formatter.format(zoneDateTime);
    } catch (Exception e) {
      return formatInstant(input, format);
    }
  }

  public static Instant convertInstantWithTimezone(String input, String format, String timezone,
      boolean reverse) throws ParseException {
    if (input == null) {
      return null;
    }
    // Get datetime UTC
    TimeZone zoneUTC = TimeZone.getTimeZone("UTC");
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    sdf.setLenient(false);
    sdf.setTimeZone(zoneUTC);
    Instant dateUTC = sdf.parse(input).toInstant();

    // Convert time utc with timezone corresponding
    try {
      ZoneId zone = ZoneId.systemDefault();
      if (timezone != null) {
        zone = ZoneId.of(timezone);
      }
      ZoneOffset offset = zone.getRules().getOffset(dateUTC);
      long offsetSecond = (long) offset.getTotalSeconds();
      if (reverse) {
        offsetSecond = -1 * offsetSecond;
      }
      return dateUTC.plusSeconds(offsetSecond);
    } catch (Exception e) {
      return convertStringToDate(input, format).toInstant();
    }
  }

  public static String convertInstantToString(Instant startDate, Locale locale, ZoneId zoneId) {
    if (startDate == null)
      return "";
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_mm_ss).withLocale(locale).withZone(zoneId);
    return formatter.format(startDate);
  }

}
