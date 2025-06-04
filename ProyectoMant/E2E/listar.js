const { chromium } = require('playwright');

(async () => {
  // Lanza el navegador en modo no headless (se abrirá la ventana del navegador)
  const browser = await chromium.launch({ headless: false });

  const page = await browser.newPage();
  await page.goto('http://localhost:8080/'); // Página de recopilatorios

  // Verificar que la página se ha cargado correctamente
  const bodyText = await page.textContent('body');
  if (bodyText.includes('Lista de artistas de la BD')) {
    console.log('Lista de recopilatorios cargada correctamente');
  }

  await page.waitForTimeout(2000);

  // Cierra el navegador
  await browser.close();
})();
