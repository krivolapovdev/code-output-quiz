import { Select } from "antd";
import { useQuizStore } from "@/shared/lib/store";
import { useSupportedProgrammingLanguages } from "../model";

export const SelectProgrammingLanguage = () => {
  const { loading, supportedProgrammingLanguages } =
    useSupportedProgrammingLanguages();
  const { setProgrammingLanguage } = useQuizStore();

  const options = loading
    ? []
    : supportedProgrammingLanguages.map(lang => ({
        value: lang.name,
        label: lang.displayName
      }));

  return (
    <Select
      showSearch
      value={useQuizStore.getState().programmingLanguage}
      className="sm:w-48 w-25"
      optionFilterProp="label"
      onChange={value => setProgrammingLanguage(value)}
      options={options}
    />
  );
};
