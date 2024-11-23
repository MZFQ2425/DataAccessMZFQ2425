CREATE OR REPLACE FUNCTION is_discount_active(
    p_product_id INT,
    p_offer_start_date DATE,
    p_offer_end_date DATE
)
RETURNS BOOLEAN AS
$$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM public.seller_products
        WHERE product_id = p_product_id
        AND NOT (offer_end_date < p_offer_start_date OR offer_start_date > p_offer_end_date)
    ) THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;
$$
LANGUAGE plpgsql;