CREATE OR REPLACE FUNCTION is_pro_discount_active(
    p_seller_id INT,
    p_offer_start_date DATE,
    p_offer_end_date DATE
)
RETURNS BOOLEAN AS
$$
BEGIN
    IF (
SELECT COUNT(seller_id)
FROM seller_products
WHERE ((offer_start_date BETWEEN p_offer_start_date AND p_offer_end_date)
    OR (offer_end_date BETWEEN p_offer_start_date AND p_offer_end_date))
  AND seller_id = p_seller_id
    )=3 THEN
        RETURN TRUE;
ELSE
        RETURN FALSE;
END IF;
END;
$$
LANGUAGE plpgsql;