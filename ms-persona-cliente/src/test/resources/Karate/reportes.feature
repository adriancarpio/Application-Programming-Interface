Feature: Obtener reportes de cuenta

  Scenario: Obtener reportes con parámetros válidos
    Given url 'http://localhost:9596'
    And path 'api/v1/movimientos/reportes'
    And param fechaInicio = '2022-08-02'
    And param fechaFin = '2022-08-02'
    And param cliente = 'Marianela Montalvo'
    When method GET
    Then status 200
    And match response ==
    """
    [
      {
        "fecha": "#notnull",
        "cliente": "#notnull",
        "numeroCuenta": "#notnull",
        "tipo": "#notnull",
        "saldoInicial": "#number",
        "estado": "#boolean",
        "movimiento": "#number",
        "saldoDisponible": "#number"
      }
    ]
    """
