CREATE TABLE dvrs (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      model VARCHAR(255) NOT NULL,
                      primary_ip VARCHAR(45),  -- Using VARCHAR(45) to accommodate both IPv4 and IPv6
                      secondary_ip VARCHAR(45),
                      created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create function to automatically update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger for dvrs table
CREATE TRIGGER update_dvrs_updated_at
    BEFORE UPDATE ON dvrs
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TABLE clips (
                       id SERIAL PRIMARY KEY,
                       clip_name VARCHAR(255) NOT NULL,
                       file_path VARCHAR(1024) NOT NULL,
                       dvr_id INTEGER NOT NULL,
                       is_hq BOOLEAN DEFAULT false,
                       start_time TIMESTAMP WITH TIME ZONE NOT NULL,
                       end_time TIMESTAMP WITH TIME ZONE NOT NULL,
                       completed_at TIMESTAMP WITH TIME ZONE,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_dvr
                           FOREIGN KEY (dvr_id)
                               REFERENCES dvrs(id)
                               ON DELETE CASCADE
);

-- Create trigger for clips table
CREATE TRIGGER update_clips_updated_at
    BEFORE UPDATE ON clips
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();