-- Sensors
CREATE TABLE tbl_sensor
(
  fld_uuid UUID NOT NULL CONSTRAINT pkey_sensor PRIMARY KEY,
  fld_status VARCHAR(10) NOT NULL,
  fld_date_status_changed TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fld_date_created TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
  --some other properties in the future
);

CREATE INDEX IF NOT EXISTS idx_fld_status ON tbl_sensor (fld_status);
CREATE INDEX IF NOT EXISTS idx_fld_date_created ON tbl_sensor (fld_date_created);


-- Measurements
CREATE TABLE tbl_measurement
(
  fld_uuid UUID NOT NULL CONSTRAINT pkey_measurement PRIMARY KEY,
  fld_sensor_id UUID NOT NULL REFERENCES tbl_sensor (fld_uuid),
  fld_co2_level INTEGER NOT NULL,
  fld_date_measured TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP

);

CREATE INDEX IF NOT EXISTS idx_fld_sensor_id ON tbl_measurement (fld_sensor_id);
CREATE INDEX IF NOT EXISTS idx_fld_date_measured ON tbl_measurement (fld_date_measured);


-- Alerts
CREATE TABLE tbl_alert
(
  fld_uuid UUID NOT NULL CONSTRAINT pkey_alert PRIMARY KEY,
  fld_sensor_id UUID NOT NULL REFERENCES tbl_sensor (fld_uuid),
  fld_first_co2_level INTEGER NOT NULL,
  fld_second_co2_level INTEGER NOT NULL,
  fld_third_co2_level INTEGER NOT NULL,
  fld_date_started TIMESTAMP WITH TIME ZONE NOT NULL,
  fld_date_ended TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_fld_sensor_id ON tbl_alert (fld_sensor_id);
CREATE INDEX IF NOT EXISTS idx_fld_date_started ON tbl_alert (fld_date_started);
CREATE INDEX IF NOT EXISTS idx_fld_date_ended ON tbl_alert (fld_date_ended);

--SELECT cron.schedule('* * * * *', $$DELETE FROM tbl_measurements WHERE fld_date_measured < now() - interval '1 month'$$);
