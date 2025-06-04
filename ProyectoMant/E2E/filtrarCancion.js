const { chromium } = require('playwright');

(async () => {
  const browser = await chromium.launch({ headless: false });
  const page = await browser.newPage();
  await page.goto('http://localhost:8080/añadirAlbum'); // Página de filtrado por género


  // Pausa para ver cómo se selecciona el género
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  // Completar las fechas
  await page.fill('input[name="fechaInicio"]', '01-01-2023');
  console.log('Fecha de inicio completada: 01-01-2023');

  // Pausa para ver cómo se completa la fecha de inicio
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  await page.fill('input[name="fechaFin"]', '31-12-2023');
  console.log('Fecha de fin completada: 31-12-2023');

  // Pausa para ver cómo se completa la fecha de fin
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  const radioButton = await page.click('input[name="idGenero"][value="1"]');  // Seleccionar el primer género con value="1"

  await page.waitForTimeout(1000);  // Espera de 1 segundo

  // Enviar el formulario de filtro
  await page.click('button[name="filtrar"]');
  console.log('Botón de filtro clickeado');

  // Pausa para ver el clic en el botón de filtro
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  // Verificar que el filtro se haya aplicado correctamente
  const bodyText = await page.textContent('body');
  if (bodyText.includes('Filtrado por género') && bodyText.includes('Fecha Inicio: 2023-01-01') && bodyText.includes('Fecha Fin: 2023-12-31')) {
    console.log('Filtro de género y fechas aplicado correctamente');
  }

  await page.waitForTimeout(1000);  // Espera de 1 segundo

  await browser.close();
})();
