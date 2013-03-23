-- USER SQL
CREATE USER "T11_name"
DEFAULT TABLESPACE "USERS"
TEMPORARY TABLESPACE "TEMP"
ACCOUNT UNLOCK ;

-- Erstellen der Rolle STUDENT und zuweisen der Rechte
CREATE ROLE "STUDENT" NOT IDENTIFIED;
GRANT CREATE MATERIALIZED VIEW TO "STUDENT";
GRANT CREATE PROCEDURE TO "STUDENT";
GRANT CREATE SEQUENCE TO "STUDENT";
GRANT CREATE SESSION TO "STUDENT";
GRANT CREATE SYNONYM TO "STUDENT";
GRANT CREATE TABLE TO "STUDENT";
GRANT CREATE TRIGGER TO "STUDENT";
GRANT CREATE TYPE TO "STUDENT";
GRANT CREATE VIEW TO "STUDENT";
-- Rolle STUDENT ist nicht erforderlich, wenn alle Rechte manuell dem Nutzer zugewiesen werden,
-- z.B. GRANT CREATE VIEW TO "T11_name" etc.

-- ROLES
ALTER USER "T11_name" DEFAULT ROLE "STUDENT";
-- nicht erforderlich, wenn alle Rechte manuell dem Nutzer zugewiesen wurden

-- SYSTEM PRIVILEGES
-- werden über die Rolle STUDENT (oder manuelles Zuweisen, s.o.) abgebildet

-- QUOTAS
ALTER USER "T11_name" QUOTA 102400K ON USERS;
-- begrenzt verfügbaren Speicherplatz
-- wäre auf eigener DB nicht zwingend erforderlich 
