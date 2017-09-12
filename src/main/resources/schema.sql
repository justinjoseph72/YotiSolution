

  DROP SEQUENCE IF EXISTS Hoover_Seq;


 CREATE SEQUENCE Hoover_Seq
   INCREMENT 1
   MINVALUE 1
   MAXVALUE 9223372036854775807
   START 9
   CACHE 1;
 ALTER TABLE Hoover_Seq
   OWNER TO postgres;





 DROP TABLE IF EXISTS  "Hoover_Table";

CREATE TABLE "Hoover_Table"
(
  id bigint NOT NULL,
  input json,
  output json,
  CONSTRAINT PRIMARY_KEY PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "Hoover_Table"
  OWNER TO postgres;









