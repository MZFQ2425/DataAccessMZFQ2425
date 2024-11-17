/*CREATE PROCEDURE*/
CREATE OR REPLACE FUNCTION show_employees_by_pattern(byPattern VARCHAR(10))
RETURNS SETOF employee
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT * FROM public.employee WHERE employee.ename LIKE '%' || byPattern || '%';
END;
$BODY$;


/*CALL THE PROCEDURE*/
SELECT * FROM show_employees_by_pattern('K');