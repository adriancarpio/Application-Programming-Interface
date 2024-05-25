Feature: Validar estructura de respuesta de cliente

  Scenario: Validar respuesta del cliente
    Given url 'http://localhost:9595'
    And path 'api/v1/clientes', 13
    When method get
    Then status 200
    And match response ==
    """
    {
      "nombre": "#string",
      "genero": "#string",
      "edad": "#number",
      "identificacion": "#string",
      "direccion": "#string",
      "telefono": "#string",
      "id": "#number",
      "contrasena": "#string",
      "estado": "#boolean"
    }
    """
