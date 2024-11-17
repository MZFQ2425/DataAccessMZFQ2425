/*Database*/
CREATE DATABASE "VTInstitute"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

/*Table with fields*/
CREATE TABLE public."Subjects"
(
    "Code" integer NOT NULL,
    "Name" character varying(50) NOT NULL,
    "Year" integer NOT NULL,
    PRIMARY KEY ("Code")
);

ALTER TABLE IF EXISTS public."Subjects"
    OWNER to postgres;

/*Create Sequence*/
CREATE SEQUENCE public."Code"
    INCREMENT 1
    START 1;

ALTER TABLE public."Subjects"
    ALTER COLUMN "Code" SET DEFAULT nextval('public."Code"');

ALTER SEQUENCE public."Code"
    OWNED BY public."Subjects"."Code";

ALTER SEQUENCE public."Code"
    OWNER TO postgres;