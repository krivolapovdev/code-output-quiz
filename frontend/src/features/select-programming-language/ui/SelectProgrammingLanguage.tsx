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
      defaultValue="Java"
      className="sm:w-48 w-25"
      optionFilterProp="label"
      filterSort={(optionA, optionB) =>
        (optionA?.label ?? "")
          .toLowerCase()
          .localeCompare((optionB?.label ?? "").toLowerCase())
      }
      onChange={value => setProgrammingLanguage(value)}
      options={options}
    />
  );
};
