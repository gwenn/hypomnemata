<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" omit-xml-declaration="yes"/>

	<xsl:variable name="line">
		<xsl:text>&#10;</xsl:text>
	</xsl:variable>
	<xsl:variable name="sep">
		<xsl:text>;</xsl:text>
	</xsl:variable>

	<xsl:template match="/">
		<xsl:for-each select="ENTITYLIST/CreditEntity">
			<xsl:variable name="CEName" select="CEName"/>			
				
			<xsl:for-each select="SnBasketItemList/SNBSKITEM">
                <xsl:variable name="CEItemName" select="CEItemName"/>
                <xsl:variable name="Weight" select="Weight"/>

                <xsl:value-of select="concat($CEName, $sep, $CEItemName, $sep, $Weight, $line)"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
