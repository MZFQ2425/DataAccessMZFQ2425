-- FUNCTION: public.get_products_not_in_store_mzfq(integer, integer)

-- DROP FUNCTION IF EXISTS public.get_products_not_in_store_mzfq(integer, integer);

CREATE OR REPLACE FUNCTION public.get_products_not_in_store_mzfq(
	p_seller_id integer,
	p_category_id integer DEFAULT NULL::integer)
    RETURNS TABLE(product_id integer, product_name character varying, description text, category_id integer, active boolean) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN
    RETURN QUERY
    SELECT p.product_id, 
           p.product_name, 
           p.description, 
           p.category_id, 
           p.active
    FROM products p
    WHERE p.product_id NOT IN (
        SELECT sp.product_id
        FROM seller_products sp
        WHERE sp.seller_id = p_seller_id
    )
    AND (p_category_id IS NULL OR p.category_id = p_category_id);
END;
$BODY$;

ALTER FUNCTION public.get_products_not_in_store_mzfq(integer, integer)
    OWNER TO postgres;
