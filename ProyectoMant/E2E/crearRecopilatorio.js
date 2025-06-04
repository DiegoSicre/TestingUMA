const { chromium } = require('playwright');

(async () => {
  const browser = await chromium.launch({ headless: false });
  const page = await browser.newPage();
  await page.goto('http://localhost:8080/añadirAlbum'); // Página de creación de recopilatorio

  // Esperar a que los elementos del formulario estén disponibles
  await page.waitForSelector('input[name="nombreRecopilatorio"]');
  await page.waitForSelector('input[name="nombreAlbum"]');
  await page.waitForSelector('select[name="idCancionesRecopilatorio"]');
  await page.waitForSelector('button[type="submit"]');

  // Completar el formulario de creación de recopilatorio
  await page.fill('input[name="nombreRecopilatorio"]', 'Recopilatorio de prueba');

  // Pausa para ver cómo se llena el primer campo
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  await page.fill('input[name="nombreAlbum"]', 'Álbum de prueba');

  // Pausa para ver cómo se llena el segundo campo
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  // Seleccionar canciones (usando los valores de las opciones del select)
  await page.selectOption('select[name="idCancionesRecopilatorio"]', { value: '1' }); // Canción con id '1'

  // Pausa para ver cómo se selecciona la canción 1
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  await page.selectOption('select[name="idCancionesRecopilatorio"]', { value: '3' }); // Canción con id '3'

  // Pausa para ver cómo se selecciona la canción 3
  await page.waitForTimeout(1000);  // Espera de 1 segundo

  // Enviar el formulario
  await page.click('button[type="submit"]');

  // Pausa antes de verificar la creación
  await page.waitForTimeout(2000);  // Espera de 2 segundos para ver la acción de envío

  // Verificar que el recopilatorio y las canciones se han creado correctamente
  const bodyText = await page.textContent('body');
  if (bodyText.includes('Recopilatorio de prueba') && bodyText.includes('Álbum de prueba') && bodyText.includes('Canción 1') && bodyText.includes('Canción 3')) {
    console.log('Recopilatorio creado correctamente con las canciones seleccionadas');
  }

  await browser.close();
})();
