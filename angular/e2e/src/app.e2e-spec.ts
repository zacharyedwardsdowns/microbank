import { AppPage } from './app.po';
import { browser, logging } from 'protractor';

describe('workspace-project App', () => {
  const pageTitle = 'MicroBank';
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
  });

  it(`should have the title ${pageTitle}`, () => {
    page.navigateTo();
    expect(page.getTitleText()).toEqual(pageTitle);
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(
      jasmine.objectContaining({
        level: logging.Level.SEVERE,
      } as logging.Entry)
    );
  });
});
