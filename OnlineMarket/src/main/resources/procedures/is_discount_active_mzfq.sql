CREATE OR REPLACE FUNCTION public.is_discount_active_mzfq(
    p_product_id integer,
    p_offer_start_date date,
    p_offer_end_date date
)
RETURNS boolean
LANGUAGE 'plpgsql'
COST 100
VOLATILE PARALLEL UNSAFE
AS $BODY$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM public.seller_products sp
        WHERE sp.product_id != p_product_id
        AND sp.seller_id = (SELECT seller_id FROM public.seller_products WHERE product_id = p_product_id LIMIT 1)
        AND (
            (sp.offer_start_date <= p_offer_end_date AND sp.offer_end_date >= p_offer_start_date)
        )
    ) THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;
$BODY$;

ALTER FUNCTION public.is_discount_active_mzfq(integer, date, date)
    OWNER TO postgres;