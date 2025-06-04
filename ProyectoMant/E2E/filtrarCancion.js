const { chromium } = require('playwright');

(async () => {
  const browser = await chromium.launch({ headless: false });
  const page = await browser.newPage();
  await page.goto('http://localhost:8080/añadirAlbum'); // Página de filtrado por género

  // Esperar a que los elementos estén disponibles
  await page.waitForSelector('input[name="idGenero"]');  // Esperar a que los radios de género estén disponibles
  await page.waitForSelector('input[name="fechaInicio"]');
  await page.waitForSelector('input[name="fechaFin"]');
  await page.waitForSelector('button[type="submit"] [name="filtrar"]'); // Esperar el botón de filtro

  // Asegurarse de que el radio button para el primer género esté disponible
  const radioButton = await page.$('input[name="idGenero"][value="1"]');  // Seleccionar el primer género con value="1"
  if (radioButton) {
    await radioButton.click();  // Clic en el radio button de género
    console.log('Género seleccionado');
  } else {
    console.log('No se encontró el radio button para el género');
  }

  // Pausa para ver cómo se selecciona el género
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  // Completar las fechas
  await page.fill('input[name="fechaInicio"]', '2023-01-01');
  console.log('Fecha de inicio completada: 2023-01-01');

  // Pausa para ver cómo se completa la fecha de inicio
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  await page.fill('input[name="fechaFin"]', '2023-12-31');
  console.log('Fecha de fin completada: 2023-12-31');

  // Pausa para ver cómo se completa la fecha de fin
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  // Enviar el formulario de filtro
  await page.click('button[type="submit"] [name="filtrar"]');
  console.log('Botón de filtro clickeado');

  // Pausa para ver el clic en el botón de filtro
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  // Esperar a que la página se recargue con los resultados filtrados
  await page.waitForNavigation({ waitUntil: 'networkidle' });

  // Verificar que el filtro se haya aplicado correctamente
  const bodyText = await page.textContent('body');
  if (bodyText.includes('Filtrado por género') && bodyText.includes('Fecha Inicio: 2023-01-01') && bodyText.includes('Fecha Fin: 2023-12-31')) {
    console.log('Filtro de género y fechas aplicado correctamente');
  }

  await browser.close();
})();
