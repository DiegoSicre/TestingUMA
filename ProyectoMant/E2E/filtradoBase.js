const { chromium } = require('playwright');

(async () => {
  const browser = await chromium.launch({ headless: false });
  const page = await browser.newPage();
  await page.goto('http://localhost:8080/');

  // Esperar a que los elementos estén disponibles
  await page.waitForSelector('input[name="cadenaFiltroNombre"]');
  await page.waitForSelector('button[type="submit"]');

  // Completar el campo de filtro por nombre (y hacer una pausa para que se vea)
  await page.fill('input[name="cadenaFiltroNombre"]', 'Recopilatorio de prueba');
  console.log('Campo de nombre completado: Recopilatorio de prueba');

  // Pausa para ver cómo se completa el campo
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  // Enviar el formulario de filtro (y hacer una pausa para ver el clic)
  await page.click('button[type="submit"]');
  console.log('Botón de filtro clickeado');

  // Pausa para ver el clic en el botón
  await page.waitForTimeout(1000);  // Espera de 1 segundo


  // Verificar que los resultados filtrados incluyen el nombre del recopilatorio
  const bodyText = await page.textContent('body');
  if (bodyText.includes('Recopilatorio de prueba')) {
    console.log('Filtrado por nombre aplicado correctamente');
  }

  await browser.close();
})();
