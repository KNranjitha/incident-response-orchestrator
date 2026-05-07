CREATE INDEX IF NOT EXISTS idx_incident_status
ON incident(status);

CREATE INDEX IF NOT EXISTS idx_incident_priority
ON incident(priority);

CREATE INDEX IF NOT EXISTS idx_incident_assigned_to
ON incident(assigned_to);