/*CREATE PROCEDURE*/
CREATE OR REPLACE FUNCTION show_employees_by_job(byJob VARCHAR(9))
RETURNS SETOF employee
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT * FROM public.employee
    WHERE job = byJob;
END;
$BODY$;

/*CALL THE PROCEDURE*/
SELECT * FROM show_employees_by_job('SALESMAN');