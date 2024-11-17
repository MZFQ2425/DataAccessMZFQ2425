/*CREATE PROCEDURE*/
CREATE OR REPLACE FUNCTION show_employees_by_dept(byDept VARCHAR(14))
RETURNS SETOF employee
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
    RETURN QUERY
    SELECT * FROM public.employee
    WHERE deptno = (
		SELECT public.dept.deptno FROM public.dept WHERE dname = byDept
	);
END;
$BODY$;

/*CALL THE PROCEDURE*/
SELECT * FROM show_employees_by_dept('ACCOUNTING');