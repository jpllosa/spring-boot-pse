package llosa.jopee.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.bson.BsonDateTime;
import org.bson.BsonNumber;
import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

import llosa.jopee.PseInvestorApplication;

// End Of Day Qoute
public class PseEodq {
	
	public static final PseEodq NULL = new PseEodq();
	
	private final String symbol;
	private final java.sql.Date date;
	private final BigDecimal open;
	private final BigDecimal high;
	private final BigDecimal low;
	private final BigDecimal close;
	private final BigDecimal volume;
	
	@JsonIgnore
	private final MathContext mathContext = new MathContext(2);
	
	@JsonIgnore
	Logger log = PseInvestorApplication.getLogger(PseEodq.class);
	
	public PseEodq(String symbol, BsonDateTime dateTime, BsonNumber open, BsonNumber high,
			BsonNumber low, BsonNumber close, BsonNumber volume) {
		this.symbol = symbol;
		Instant instant = Instant.ofEpochMilli(dateTime.getValue());
		log.info("PseEodq: " + instant);
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Europe/London"));
		log.info("localDateTime: " + localDateTime);

		this.date = java.sql.Date.valueOf(localDateTime.toLocalDate());
		this.open = new BigDecimal(open.doubleValue(), mathContext);
		this.high = new BigDecimal(high.doubleValue(), mathContext);
		this.low = new BigDecimal(low.doubleValue(), mathContext);
		this.close = new BigDecimal(""+close.doubleValue());
		this.volume = new BigDecimal(volume.doubleValue(), mathContext);
	}

	public PseEodq() {
		this.symbol = "";
		this.date = new java.sql.Date(0);
		this.open = new BigDecimal("0.00");
		this.high = this.open;
		this.low = this.open;
		this.close = this.open;
		this.volume = this.open;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public java.sql.Date getDate() {
		return date;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public BigDecimal getClose() {
		return close;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public MathContext getMathContext() {
		return mathContext;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((close == null) ? 0 : close.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((high == null) ? 0 : high.hashCode());
		result = prime * result + ((low == null) ? 0 : low.hashCode());
		result = prime * result + ((mathContext == null) ? 0 : mathContext.hashCode());
		result = prime * result + ((open == null) ? 0 : open.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + ((volume == null) ? 0 : volume.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PseEodq other = (PseEodq) obj;
		if (close == null) {
			if (other.close != null)
				return false;
		} else if (!close.equals(other.close))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (high == null) {
			if (other.high != null)
				return false;
		} else if (!high.equals(other.high))
			return false;
		if (low == null) {
			if (other.low != null)
				return false;
		} else if (!low.equals(other.low))
			return false;
		if (mathContext == null) {
			if (other.mathContext != null)
				return false;
		} else if (!mathContext.equals(other.mathContext))
			return false;
		if (open == null) {
			if (other.open != null)
				return false;
		} else if (!open.equals(other.open))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		if (volume == null) {
			if (other.volume != null)
				return false;
		} else if (!volume.equals(other.volume))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PseEodq [symbol=" + symbol + ", date=" + date + ", open=" + open + ", high=" + high + ", low=" + low
				+ ", close=" + close + ", volume=" + volume + ", mathContext=" + mathContext + "]";
	}
	
}
